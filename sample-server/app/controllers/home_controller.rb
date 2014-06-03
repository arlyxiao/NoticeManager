class HomeController < ApplicationController

  def index

    token = 'thisisforrepeatnotice'
    
    if params[:token] == token
      title = '测试 title'
      desc = '测试 desc'
      other = '测试 other'

      render :json => {
        :title => title,
        :desc => desc,
        :other => other
      }
    end


    
  end

end
